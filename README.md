# Kemu Kupu Game
<img src="./logo.png" style="height:11em;">

## **Note this project is archived, and no longer in development.**

<br>

## Playing the Game
**This game is validated to work on the following platforms:**
- Ubuntu 20.04 LTS
- Elementary OS 5.0 Juno
- Fedora 34

**Ensure that the required files are available:**
- Festival support for the Maori Language.
- JavaFX 11 files located in `/home/student/javafx-sdk-11.0.2/javafx`
    - Note that this can be changed by editing the final line of `runQuiz.sh` if you have a different install location.
- To use the login features, the api must be setup at kemukupu.com. Installation information can be found [here](https://github.com/kemukupu/game). Note that the game may be run without this api being active, but not all functionality will work correctly. If you wish to compile a version of the game that does not require the api, feel free to follow the instructions below for compiling Kemu Kupu.

**To play the game:**
```sh
#Clone and enter repo
git clone https://github.com/kemukupu/game --recursive
cd ./game

#Run the quiz jar file
chmod +x runQuiz.sh
./runQuiz.sh
```

**Note that for optimal results, please ensure Kemu Kupu is run on the VM image provided for SE206**

## Developing for Kemu Kupu
To develop for Kemu Kupu, please install relevant development tools.
### Fedora

```sh
git clone https://github.com/kemukupu/game --recursive
# Install Java Dependencies
sudo dnf install java-11-openjdk-devel java-11-openjdk festival maven

#Start Backend Server Instance
cd ./api
cp .example.env .env
nano .env #update relevant details for your install
docker-compose --env-file .env up

#Edit line 26 of `./src/main/java/com/util/API.java` to use local api
code . #Begin working!

#Useful Maven Commands
#Note that Jlink may fail if your JAVA_HOME env var is not set.
mvn clean verify #run tests + check compilation
```