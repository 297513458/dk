pipeline {
    agent any
    environment {
        def giturl='git@121.40.208.242:midgard/activity-service.git'
        def project_module_path='activity-service'
        //发布的名称 
       def deploy_name='activity-service'
       //hub或私服的用户名 
       def name ='297513458'
       //服务开放的端口
       def target_port=2889
       //k8s的namespace
       def namespace='app'
       def branch='dk_k8s'
       //hub地址
       def hub_host='registry-vpc.cn-shanghai.aliyuncs.com'
       //
       def hub_name="$hub_host/$name"
        def credentialsId='d3eea6a-fc42-40ce-9b27-d3778f045a05'
               //dk私服密码 
       def credentialsId_hub='530e3fb7-d2d7-4feb-8778-1f5be5b7549e'
       def replicas=1
   }
    stages{
      stage('从git拉取'  ) {
           steps {
               git credentialsId:"$credentialsId",branch: "$branch", url: "$giturl"
           }
       }
       stage('maven构建') { 
           steps {
                echo 'mvn clean package'
                sh "echo `date +%Y%m%d%H-`$env.BUILD_ID > build.v"
                sh "mvn clean package -Dmaven.test.skip=true"
           }
       }
        stage('构建docker') {  
            steps {
                echo 'build docker'
                 sh '''
                    v=`cat build.v`
                    cd $project_module_path
                    echo '\nRUN echo \'PINPOINT_NAME=\"${deploy_name}\"\' >> /opt/pinpoint-env.sh' >> Dockerfile
                    docker build -t $hub_name/${deploy_name}:$v .
                  '''}
       }
       stage('发布到私服 ') {
           steps { 
               echo 'deploy docker'
              withCredentials([usernamePassword(credentialsId:"$credentialsId_hub", passwordVariable: 'login_password', usernameVariable: 'login_name')]) {
                 sh '''
                 v=`cat build.v`
                 docker login -u $login_name -p $login_password $hub_host
                 docker push $hub_name/${deploy_name}:$v
                 '''
                }
           }
       }
        stage('触发k8s') {
           steps { 
               echo 'deploy k8s'
                sh '''
                   v=`cat build.v`
                   count=`kubectl get deploy ${deploy_name} --namespace=$namespace|wc -l`
                   if [ $count == 2 ]
                   then
                        echo "exec update"
                        kubectl set image deployments/$deploy_name $deploy_name=$hub_name/${deploy_name}:$v --namespace=$namespace
                    else
                        echo "exec deploy"
                    kubectl run ${deploy_name} --image=$hub_name/${deploy_name}:$v --replicas=$replicas --namespace=$namespace
                    echo "开放服务k8s"
                    kubectl expose deployment/${deploy_name} --port=${target_port}  --target-port=${target_port} --type=LoadBalancer --namespace=$namespace
                fi
             '''
           }
        }
   }
}
