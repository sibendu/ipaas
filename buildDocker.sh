cd coms-ui
docker build -t registry.gitlab.com/jucoms/coms/coms-ui .
docker push registry.gitlab.com/jucoms/coms/coms-ui 

cd ..

cd coms-api
docker build -t registry.gitlab.com/jucoms/coms/coms-api .
docker push registry.gitlab.com/jucoms/coms/coms-api
