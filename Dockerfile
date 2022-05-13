# Settings.
ARG USER_ID=3001
ARG USER_NAME=cap
ARG HOME_DIR=/$USER_NAME
ARG SOURCE_DIR=$HOME_DIR/source

# Maven stage.
FROM maven:3-openjdk-11-slim as maven
ARG USER_ID
ARG USER_NAME
ARG HOME_DIR
ARG SOURCE_DIR

# Create the group (use a high ID to attempt to avoid conflits).
RUN groupadd -g $USER_ID $USER_NAME

# Create the user (use a high ID to attempt to avoid conflits).
RUN useradd -d $HOME_DIR -m -u $USER_ID -g $USER_ID $USER_NAME

# Update the system.
RUN apt-get update && apt-get upgrade -y

# Install Nodejs and npm.
RUN apt-get install -y nodejs
RUN apt-get install -y npm

# Set deployment directory.
WORKDIR $SOURCE_DIR

# Copy files over.
COPY ./pom.xml ./pom.xml
COPY ./src ./src
COPY ./package.json ./package.json
COPY ./Gruntfile.js ./Gruntfile.js
COPY ./.jshintrc ./.jshintrc

# Assign file permissions.
RUN chown -R ${USER_ID}:${USER_ID} ${SOURCE_DIR}

# Install grunt.
RUN npm install -g grunt-cli

# Build.
RUN mvn package -DskipTests=true -Dprod -Dpackaging=jar

# Switch to Normal JRE Stage.
FROM openjdk:11-jre-slim
ARG USER_ID
ARG USER_NAME
ARG HOME_DIR
ARG SOURCE_DIR

# Create the group (use a high ID to attempt to avoid conflits).
RUN groupadd -g $USER_ID $USER_NAME

# Create the user (use a high ID to attempt to avoid conflits).
RUN useradd -d $HOME_DIR -m -u $USER_ID -g $USER_ID $USER_NAME

# Login as user.
USER $USER_NAME

# Set deployment directory.
WORKDIR $HOME_DIR

# Copy over the built artifact and library from the maven image.
COPY --from=maven $SOURCE_DIR/target/ROOT.jar ./cap.jar
COPY --from=maven $SOURCE_DIR/target/libs ./libs

# Run java command.
CMD ["java", "-jar", "./cap.jar"]