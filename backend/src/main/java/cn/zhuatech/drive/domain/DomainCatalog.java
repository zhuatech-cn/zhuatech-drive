/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.drive.domain;
import org.springframework.stereotype.Component;
import java.util.*;
@Component
public class DomainCatalog {
    private final Map<String, WorkflowAction> actions = new LinkedHashMap<>();
    public DomainCatalog() {
        actions.put("SUBMIT", new WorkflowAction("SUBMIT", "提交文件发布", List.of("草稿"), "待审核", "OPERATOR"));
        actions.put("APPROVE", new WorkflowAction("APPROVE", "批准文档发布", List.of("待审核"), "已发布", "ADMIN"));
        actions.put("ARCHIVE", new WorkflowAction("ARCHIVE", "归档受控文档", List.of("已发布"), "已归档", "ADMIN"));
    }
    public String systemName() { return "知华科技企业网盘与文档管理系统"; }
    public String scene() { return "团队空间、文件、版本、在线预览、共享、权限、水印、保留、回收站、检索与审计"; }
    public String initialStatus() { return "草稿"; }
    public String partyLabel() { return "组织/团队空间"; }
    public String amountLabel() { return "存储价值"; }
    public String quantityLabel() { return "文件数量"; }
    public String dueLabel() { return "共享或保留期限"; }
    public List<ModuleDefinition> modules() { return List.of(
            new ModuleDefinition("SPACE", "企业与团队空间", "按租户、组织、项目设置容量、负责人和数据边界"),
            new ModuleDefinition("FILE", "文件管理", "支持目录、上传、分片、秒传、预览、下载和回收站"),
            new ModuleDefinition("VERSION", "版本协作", "保存版本、校验哈希、锁定、评论和历史恢复"),
            new ModuleDefinition("PERMISSION", "精细权限", "支持继承、用户、部门、角色和最小权限控制"),
            new ModuleDefinition("SHARING", "安全共享", "配置密码、有效期、下载限制、审批和撤销"),
            new ModuleDefinition("WATERMARK", "水印与防泄漏", "按敏感级别应用预览水印、下载控制和告警"),
            new ModuleDefinition("LIFECYCLE", "文档生命周期", "执行保留、归档、冻结、到期复核和合规删除"),
            new ModuleDefinition("SEARCH", "全文检索", "检索名称、标签、正文、所有者、版本和权限范围"),
            new ModuleDefinition("AUDIT", "访问审计", "记录上传、预览、下载、共享、授权和删除行为")
        ); }
    public Map<String, WorkflowAction> actions() { return Collections.unmodifiableMap(actions); }
    public record ModuleDefinition(String code,String name,String description) {}
    public record WorkflowAction(String code,String label,List<String> from,String to,String requiredRole) {}
}
