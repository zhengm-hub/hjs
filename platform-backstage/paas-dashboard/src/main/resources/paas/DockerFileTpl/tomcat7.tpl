# 基础镜像
FROM ${Registry}/library/tomcat:7.0.54

ADD ${Tarball} ${AppDir}/

WORKDIR ${AppDir}

# 解压上传的war包到/app_home目录下

RUN unzip -o -d /app_home ${Tarball} && rm -rf ${Tarball}

# 暴露的端口
EXPOSE 8080

# 启动容器执行的命令
# CMD ["/opt/bin/control", "start", "8080"]
CMD bash /usr/local/tomcat/bin/catalina.sh run