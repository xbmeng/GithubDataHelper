# github-datamanager
敏捷软件开发


## model
1. UserFollow 用户关注信息  
存储关注信息

|  字段   | 含义  |
|  :----:  | :----:  |
| userId  | 用户id(关注者) |
| following  | 正在关注的id |
2. RepoComment 
评论信息

|  字段   | 含义  |
|  :----:  | :----:  |
| userId  | 用户id |
| repoId  | 仓库id |
| commitId  | commitid |
| line  | 评论所在行 |
| commentCreatedTime  | 评论创建时间 |
| commentUpdateTime  | 评论更新时间 |
| path  | 评论文件路径 |
| body | 评论具体内容 |
3. GithubUser  
用户信息

|  字段   | 含义  |
|  :----:  | :----:  |
| userName  | 用户名 |
| joiningTime  | 加入时间 |
| langusge  | 语言信息 |
| stars  | 获得star数 |
| repoNumber  | 仓库数 |
| followingNumber  | 正在关注人数 |
| followersNumber  | 关注此用户的人数 |
| reposInfo | 仓库信息 |

4. 