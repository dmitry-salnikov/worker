#!/bin/bash
# If the first argument to the 'build.sh' is '--no-sign', then we should skip
# sending the APK to the deployment server for signing.
[[ '--no-sign' == "$1" ]] && sign=false || sign=true;

# Path for build related folders.
BUILD="./app/build";
OUTPUT="${BUILD}/outputs/apk";

UNSIGNED_APK="app-release-unsigned.apk";
UNALIGNED_APK="app-release-unaligned.apk";
RELEASE_APK="app-release.apk";

# Name of the signing host.
HOST="android-deployment";

# Check if command is available in the path.
function isCommandAvailable {
    # Check that the command is available.
    $(which $1 >/dev/null 2>/dev/null);
    if [ $? -eq 1 ]; then
        echo "Unable to locate $1";
        exit;
    fi;
}

# Check that required commands are available.
isCommandAvailable java;

# Only if we are signing the APK do we need the zipalign command.
if [ $sign = true ]; then
    isCommandAvailable zipalign;
fi;

# Clean the build related files, these will be generated
# again when the release is assembled.
[ -f $REALEASE_APK ] && rm -f $RELEASE_APK;
[ -f $UNALIGNED_APK ] && rm -f $UNALIGNED_APK;

# Clean, test, and assemble the APK.
./gradlew \
    clean \
    test \
    assemble

# Check if we should send the APK to the deployment server for signing.
if [ $sign = true ]; then
    # Before we start deploying the APK for signing we have to do
    # some initial clean up. Removing the unsigned and unaligned
    # files from the deployment host, if they still exists.
    $(ssh $HOST rm -f $UNSIGNED_APK &>/dev/null);
    $(ssh $HOST rm -f $UNALIGNED_APK &>/dev/null);

    # Send the unsigned release APK to the signing host.
    scp "${OUTPUT}/${UNSIGNED_APK}" "${HOST}:${UNSIGNED_APK}";

    # Open a new connection to the signing host, we need to enter
    # the password for the keystore. And, since we don't want the
    # to store the password in plain text anywhere it's better to
    # open a new connection and manually enter the password.
    ssh $HOST;

    # Verify that the unaligned apk actually exists on the signing
    # host before we attempt to retrieve it.
    if $(ssh -q $HOST [[ ! -f $UNALIGNED_APK ]]); then
        echo "Unable to locate ${UNALIGNED_APK} on signing host";
        exit 1;
    fi;

    # Retrieve the unaligned apk from the signing host and
    # initialize the alignment.
    scp "${HOST}:${UNALIGNED_APK}" $UNALIGNED_APK;
    zipalign -v 4 $UNALIGNED_APK $RELEASE_APK;

    # Do some clean up and exit.
    [ -f $UNALIGNED_APK ] && rm -f $UNALIGNED_APK;
fi;
exit 0;