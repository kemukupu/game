#!/usr/bin/env bash

#Author: Group 11
#About: This is a small script to automatically run the parent application after packaging.

declare -i installed=1;
declare -i updated=0;

#Helper function which asks if a user would like to install a package
# $1 The name of the package to install
# $2 Whether the package is optional
function install_package() {
    declare local __action="n";
    declare -a __acceptable_commands=("n" "y");

    read -p "Would you like to install '$1' now? [y/n] " __action;
    while [[ ! " ${__acceptable_commands[@]} " =~ " ${__action} " ]]
    do
        read -p "Invalid input, try again: [y/n]: " __action;
    done

    if [[ "$__action" == "y" ]];
    then
        if [[ $updated == 0 ]];
        then
            sudo apt-get update && sudo apt-get upgrade -y;
            updated=1;
        fi

        sudo apt-get install -y $1;
    else
        echo "Installation declined";
        if [[ $2 == 0 ]];
        then
            installed=0;
        fi
    fi
}

#Helper function which checks if package is installed, and asks if the user would like to install it.
# $1 String: name of dependency to install
# $2 bool (0 | 1): optional depedency
function check_dependency() {
    declare local __package_name=$1;
    declare local __is_optional=$2;
    if [[ $__is_optional == 0 ]];
    then
        echo "Checking required dependency '$__package_name'";
    else
        echo "Checking optional dependency '$__package_name'";
    fi

    if [ $(dpkg-query -W -f='${Status}' "$__package_name" 2>/dev/null | grep -c "ok installed") -eq 0 ];
    then
        echo "Package not installed!";
        install_package $__package_name $__is_optional;
    else
        echo "Dependency installed!";
    fi
    echo "";
}

#Check the user has all required dependencies installed:
check_dependency "festival" 0;
check_dependency "openjdk-11-jdk" 0;
check_dependency "gtk2-engines-pixbuf" 1;
check_dependency "libcanberra-gtk-module" 1;


if [[ $installed == 0 ]];
then
    declare local __action="n";
    echo "Not all required dependencies installed, this program may not function as intended.";
    read -p "Are you sure you want to continue? [y/N]" __action;
    if [[ ${__action,,} != "y" ]];
    then
        exit 0;
    fi
fi

mkdir -p ./.user/;
touch ./.user/.userStats.txt;

#Start Application
/usr/lib/jvm/java-11-openjdk-amd64/bin/java -Djdk.gtk.version=2 --module-path /home/student/javafx-sdk-11.0.2/javafx --add-modules javafx.controls,javafx.media,javafx.base,javafx.fxml -Dfile.encoding=UTF-8 -jar quiz.jar