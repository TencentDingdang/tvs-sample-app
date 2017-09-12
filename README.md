# tvs-sample-app
productID：AppKey:AppAccessToken

示例b0851325-3056-4853-921b-dcba21b491a3:8c901ad100ad44d98b6276adeb861058

DSN：序列号

1、下载安装VLC Media Player，注意版本（32位/64位）必须与JDK版本类型一致，JDK至少为JDK8以上版本

2、配置VLC环境变量字段，VLC_PATH为C:\Program Files\VideoLAN\VLC

3、下载安装Node.js

4、下载安装OpenSSL，并把OpenSSL的bin目录配到Windows Path环境变量下

5、设置OpenSSL环境变量字段，OPENSSL_CONF为OpenSSL目录\bin\openssl.cfg

6、打开目录javaclient里的ssl.cnf文件，填入所有带YOUR开头的字段，注意countryName必须为两个字母，如CN

7、进入javaclient目录，打开命令行，运行generate.bat

8、进入androidCompanionApp/app/，打开命令行，
运行keytool -genkey -v -keystore keystore.jks -alias androiddebugkey -keyalg RSA -sigalg SHA1withRSA -keysize 2048 -validity 10000，
所有password字段输入android

9、运行keytool -list -v -alias androiddebugkey -keystore keystore.jks，所有password字段输入android，保存MD5和SHA256字段

10、在签名生成网站上输入包名和以上两个字段信息，生成api_key放到androidCompanionApp/app/src/main/assets/api_key.txt里
