## 项目介绍

用于自动化部署

## 打包

在项目下执行

```
mvn clean package 
```

即可完成打包

## 运行

解压打包生成的压缩包，指定脚本已完成相关操作，数据采集的，重复执行会覆盖掉相同项目的数据。数据库文件复制到当前目录下的
`db` 文件夹即可

## 操作流程

### 1. 执行采集数据脚本，采集正确部署的项目信息（采集文件大小，MD5）
### 2. 将数据库和相关war复制到现场的指定目录
### 3. 检查本程序的配置文件，如`war包目录`,`部署目录`,`配置文件目录`
### 4. 执行部署脚本，完成部署操作（删除原部署目录的war，自动将新的war复制到部署目录，备份原来的部署文件，解压新的war，替换配置文件）
### 5. 执行数据校验脚本，自动扫描新部署的文件，与数据库中采集的数据做比较，不匹配的文件给出日志

### 目录介绍

##### `conf`：配置文件目录
#### `db`：数据库文件目录

### 项目依赖

[teclan.exec](https://github.com/teclan/teclan-exec)
[teclean.guice](https://github.com/teclan/teclean.guice)
[teclan.utils](https://github.com/teclan/teclan.utils)



### 相关配置请查看以下配置文件说明

```
config {
   
  deploy {
  
    # 部署的项目名称列表，下方需要跟随相关配置，如需添加项目，按照格式添加配置即可
    projects=["IntegratedMM","AlertProcessing","RDAcenter","Event-Processing-Nodes"]
    
    # 管理平台配置
    IntegratedMM {
    
	     # 采集路径，最后的目录后面不能写 \\
	     collect_path="E:\\Repository\\Codes\\java\\IntegratedMM\\target\\IntegratedMM"
	     
	     # war 包路径
	     war_path="E:\\Repository\\Codes\\java\\IntegratedMM\\target\\IntegratedMM.war"
	     
	     # 部署路径
	     deploy_path="E:\\deploy\\"
	     
	     # 正确的配置文件路径 
	     config_file_path="E:\\deploy\\配置文件\\IntegratedMM"
    }
  
    # 事件服务配置
    AlertProcessing {
    
	     # 采集路径，最后的目录后面不能写 \\
	     collect_path="E:\\Repository\\Codes\\java\\AlertProcessing\\target\\AlertProcessing"
	     
	     # war 包路径
	     war_path="E:\\Repository\\Codes\\java\\AlertProcessing\\target\\AlertProcessing.war"
	     
	     # 部署路径
	     deploy_path="E:\\deploy\\"
	     
	     # 正确的配置文件路径 
	     config_file_path="E:\\deploy\\配置文件\\AlertProcessing"
    }
    
     # 联网配置
    RDAcenter {
    
	     # 采集路径，最后的目录后面不能写 \\
	     collect_path="E:\\Repository\\Codes\\java\\RDAcenter\\target\\RDAcenter"
	     
	     # war 包路径
	     war_path="E:\\Repository\\Codes\\java\\RDAcenter\\target\\RDAcenter.war"
	     
	     # 部署路径
	     deploy_path="E:\\deploy\\"
	     
	     # 正确的配置文件路径 
	     config_file_path="E:\\deploy\\配置文件\\RDAcenter"
    }
    
    
     # 事件服务配置
    Event-Processing-Nodes {
    
	     # 采集路径，最后的目录后面不能写 \\
	     collect_path="E:\\Repository\\Codes\\java\\Event-Processing-Nodes\\target\\Event-Processing-Nodes"
	     
	     # war 包路径
	     war_path="E:\\Repository\\Codes\\java\\Event-Processing-Nodes\\target\\Event-Processing-Nodes.war"
	     
	     # 部署路径
	     deploy_path="E:\\deploy\\"
	     
	     # 正确的配置文件路径 
	     config_file_path="E:\\deploy\\配置文件\\Event-Processing-Nodes"
    }
  
  
  }
 
 ########################################################################
 #                       以下配置请勿随意修改								#
 ########################################################################

  ## 数据库配置项
  db {
    ## 数据库连接名称
    name = "default"

    ## 数据库迁移配置项
    migration {
      ## 应用启动时是否执行迁移
      migrate = true
    }

    ## JDBC连接配置项
    jdbc {
      ## 连接驱动
      driver   = "org.h2.Driver"
      
      url-template:"jdbc:h2:file:%s"

      ## 数据库文件相对应用的存放路径
      db-path      = "db/teclan-monitor"
      
      ## 用户名
      user     = "system"
      
      ## 密码
      password = "65536"
    }
  }
  thread.max=2
}

```
