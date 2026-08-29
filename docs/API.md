# 企业网盘与文档管理系统 API

所有业务接口默认位于 `/api`，除 `/public/**` 和健康检查外均需要 HTTP Basic 身份认证。生产环境应接入企业 IAM 或统一身份平台。

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/public/about` | 产品、公司、官网和许可元数据 |
| GET | `/catalog` | 业务模块、字段标签和状态动作 |
| GET | `/dashboard` | 业务规模、金额、状态和模块统计 |
| GET/POST | `/records` | 业务台账查询与创建 |
| GET/PUT/DELETE | `/records/{id}` | 详情、草稿修改与删除 |
| POST | `/records/{id}/actions` | 执行服务端状态迁移 |
| POST | `/records/{id}/comments` | 增加协作记录 |
| GET | `/records/{id}/timeline` | 查询完整操作时间线 |
| GET | `/records/search` | 组合检索、分页和逾期筛选 |
| GET | `/records/export.csv` | 导出 UTF-8 CSV |
| GET | `/sla-summary` | SLA、逾期、风险和人员工作量 |
| POST | `/domain/decision` | 执行企业网盘与文档管理系统专属领域规则 |
| GET/POST | `/enterprise/controls` | 企业控制项查询与幂等创建 |
| POST | `/enterprise/controls/{id}/submit` | 提交复核 |
| POST | `/admin/enterprise/controls/{id}/review` | 管理员审批或驳回 |
| POST | `/enterprise/controls/{id}/documents` | 登记附件哈希及存储元数据 |
| POST | `/enterprise/controls/{id}/complete` | 凭证完整后办结 |
| POST | `/admin/enterprise/controls/{id}/sync` | 登记外部系统回执 |

## 领域决策字段

| 字段 | 类型 | 含义 |
| --- | --- | --- |
| `fileNo` | String | 文件编号 |
| `fileSizeBytes` | long | 文件大小(字节) |
| `versionCount` | int | 版本数量 |
| `classification` | String | 数据分级 |
| `shareDays` | int | 共享有效天数 |
| `retentionDays` | int | 保留期限(天) |
| `checksumVerified` | boolean | 文件哈希已校验 |
| `permissionReviewed` | boolean | 访问权限已复核 |
| `watermarkEnabled` | boolean | 已启用水印 |

接口统一返回 `ApiResponse`；业务冲突使用 HTTP 409，参数错误使用 400，未认证使用 401，无权限使用 403。

## 文档核心 API

`POST/GET /core/drive/documents` 管理受控文档，`POST/GET /core/drive/documents/{id}/versions` 管理不可变版本。管理员通过 `/admin/core/drive/documents/{id}/shares` 创建安全共享，通过 `/archive` 归档文档；访问者使用 `POST /public/drive/shares/{token}/access` 校验密码、期限和下载次数。响应永不包含密码哈希。

`POST /core/drive/documents/{id}/checkout` 建立签出锁，`/checkin` 仅允许当前签出人提交新版本并释放锁；管理员可调用 `/admin/core/drive/documents/{id}/force-unlock` 处理异常锁定。
