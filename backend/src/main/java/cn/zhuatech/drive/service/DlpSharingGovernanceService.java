/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.drive.service;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Service
public class DlpSharingGovernanceService {
    private static final Set<String> LEVELS = Set.of("PUBLIC", "INTERNAL", "CONFIDENTIAL", "RESTRICTED");
    public Result evaluate(Request request) {
        List<String> blockers = new ArrayList<>();
        if (!request.malwareScanPassed()) blockers.add("文件安全扫描未通过");
        if (request.legalHoldActive() && request.externalSharing()) blockers.add("法律保全文件禁止外部分享");
        if ("RESTRICTED".equals(request.classification()) && request.externalSharing()) blockers.add("受限文件禁止外部分享");
        if (request.externalSharing() && !request.ownerApproved()) blockers.add("外部分享缺少数据责任人批准");
        if (request.externalSharing() && !request.mfaEnforced()) blockers.add("外部访问未强制多因素认证");
        if (request.externalSharing() && (request.expiryDays() < 1 || request.expiryDays() > 30)) blockers.add("外部链接有效期必须为 1 至 30 天");
        String decision = blockers.isEmpty() ? "ALLOW" : "DENY";
        return new Result(request.fileId(), decision, request.classification(), List.copyOf(blockers), blockers.isEmpty());
    }
    public record Request(@NotBlank String fileId, @NotBlank String classification,
                          boolean externalSharing, boolean ownerApproved, boolean mfaEnforced,
                          @Min(0) int expiryDays, boolean legalHoldActive, boolean malwareScanPassed) {
        public Request {
            if (fileId == null || fileId.isBlank()) throw new IllegalArgumentException("fileId is required");
            if (!LEVELS.contains(classification)) throw new IllegalArgumentException("invalid classification");
            if (expiryDays < 0) throw new IllegalArgumentException("expiryDays must be non-negative");
        }
    }
    public record Result(String fileId, String decision, String classification,
                         List<String> blockers, boolean linkCreationAllowed) {}
}
