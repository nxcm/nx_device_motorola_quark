#!/system/bin/sh
PATH=/sbin:/system/sbin:/system/bin:/system/xbin
export PATH

# These are the common values
setprop "ro.build.product" "quark"
setprop "ro.product.device" "quark"

# Check fsg to determine the model

# XT1225 - SINGLELA
ls /fsg | grep quark_singlela > /dev/null 2> /dev/null
if [ $? -eq 0 ]; then
    setprop "ro.fsg-id" "singlela"
    setprop "ro.product.model" "Moto Maxx"
    setprop "ro.nx.model.name" "Moto Maxx"
    setprop "ro.build.description" "quark_retla-user 5.0.2 LXG22.33-12.16 16 release-keys"
    setprop "ro.build.fingerprint" "motorola/quark_retla/quark_umts:5.0.2/LXG22.33-12.16/16:user/release-keys"
    exit 0
fi

# XT1225 - RETEU
ls /fsg | grep quark_emea > /dev/null 2> /dev/null
if [ $? -eq 0 ]; then
    setprop "ro.fsg-id" "emea"
    setprop "ro.product.model" "Moto Turbo"
    setprop "ro.nx.model.name" "Moto Turbo"
    setprop "ro.build.description" "quark_reteu-user 5.0.2 LXG22.33-12.16 16 release-keys"
    setprop "ro.build.fingerprint" "motorola/quark_reteu/quark_umts:5.0.2/LXG22.33-12.16/16:user/release-keys"
    exit 0
fi

# Unknown model, try to use ro.boot.carrier
CARRIER=$(getprop "ro.boot.carrier")

setprop "ro.fsg-id" "$CARRIER"
setprop "ro.product.model" "Moto XT1225 ($CARRIER)"
setprop "ro.nx.model.name" "Moto XT1225 ($CARRIER)"
setprop "ro.build.description" "quark_retla-user 5.0.2 LXG22.33-12.16 16 release-keys"
setprop "ro.build.fingerprint" "motorola/quark_retla/quark_umts:5.0.2/LXG22.33-12.16/16:user/release-keys"
exit 1
