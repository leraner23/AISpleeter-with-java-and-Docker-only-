# AISpleeter-with-java-and-Docker-only-
This is a small java based project with docker to use AISpleeter to split the music into vocal and Instruments sounds.

The system uses a Spring + Docker flow to split uploaded audio files.
1. Upload: A user posts an audio file to the controller, which saves the file into a temporary upload directory on the server.
2. Processing (Docker): The service component runs a docker run command using the deezer/spleeter:3.7 image.
3. Volume Mounting: Key to this process are two volume mounts (-v): the host input directory is mounted as /input, and a host output directory is mounted as /output inside the container.
4. Spleeter Execution: Inside the container, Spleeter separates the file using the 2stems preset (vocals and accompaniment) and writes the resulting vocals.wav and accompaniment.wav into the mapped /output directory.
5. Download: Since the output directory is mounted back to the host, the application can read the files and provide links for the user to download them
