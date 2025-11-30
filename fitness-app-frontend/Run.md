# build the image (tag it)

docker build -t fitness-app:latest .

# run it (map port 80)

docker run -d --name fitness-app-prod -p 8080:80 fitness-app:latest

# open http://localhost:8080

docker compose -f docker-compose.dev.yml up --build

# open http://localhost:5173

docker compose -f docker-compose.prod.yml up --build -d

# open http://localhost:8080

# stop the container

docker stop fitness-app-prod

# remove the container

docker rm fitness-app-prod

# remove the image

docker rmi fitness-app:latest
