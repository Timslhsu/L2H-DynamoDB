# Prepare Environment

1. Create an EC2 role with AmazonDynamoDBFullAccess policy.
1. Launch an EC2 instance using the AMI "Amazon Linux 2 AMI (HVM), SSD Volume Type", and attach the EC2 role to the instance.
1. SSH into the instance, and run the following commands

```bash
cd ~
sudo yum install -y java-1.8.0-openjdk-devel git python3.x86_64
sudo pip3 install boto3
wget https://sdk-for-java.amazonwebservices.com/latest/aws-java-sdk.zip
unzip aws-java-sdk.zip
mkdir JARS
cp ~/aws-java-sdk-1.11.671/lib/aws-java-sdk-1.11.671.jar ~/JARS/
cp ~/aws-java-sdk-1.11.671/third-party/lib/*.jar ~/JARS/
wget https://www-eu.apache.org/dist/logging/log4j/1.2.17/log4j-1.2.17.tar.gz
tar zxvf log4j-1.2.17.tar.gz
cp ~/apache-log4j-1.2.17/log4j-1.2.17.jar ~/JARS/
export CLASSPATH=~/JARS/*:.
echo export CLASSPATH=~/JARS/*:. >> ~/.bashrc
git clone https://github.com/Timslhsu/L2H-DynamoDB.git

```

Finally, execute `aws configure` to set default region name, e.g. ap-northeast-1
