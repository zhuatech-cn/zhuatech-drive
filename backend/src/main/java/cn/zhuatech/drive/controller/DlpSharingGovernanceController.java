/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.drive.controller;
import cn.zhuatech.drive.common.ApiResponse;
import cn.zhuatech.drive.service.DlpSharingGovernanceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/enterprise/drive")
public class DlpSharingGovernanceController {
    private final DlpSharingGovernanceService service;
    public DlpSharingGovernanceController(DlpSharingGovernanceService service) { this.service = service; }
    @PostMapping("/sharing-governance")
    public ApiResponse<DlpSharingGovernanceService.Result> evaluate(@Valid @RequestBody DlpSharingGovernanceService.Request request) {
        return ApiResponse.ok(service.evaluate(request));
    }
}
