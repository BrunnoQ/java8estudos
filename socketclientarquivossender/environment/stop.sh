ps -ef | grep socketclientsinais.jar | awk '{print $2}' | xargs kill -9 > /dev/null 2>&1&
echo Processo Finilizado