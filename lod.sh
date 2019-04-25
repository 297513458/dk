#/bin/bash

file="images"

if [ -f "$file" ]
then
  echo "$file found."

  while IFS='=' read -r key value
  do
  echo "docker pull ${value} ${key}"
   # docker pull ${value}
    #docker tag ${value} ${key}
    docker rmi ${key}
  done < "$file"

else
  echo "$file not found."
fi
