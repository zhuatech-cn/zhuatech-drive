/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.drive.controller;import cn.zhuatech.drive.common.ApiResponse;import cn.zhuatech.drive.service.DriveCoreService;import jakarta.validation.Valid;import org.springframework.web.bind.annotation.*;import java.util.List;
@RestController public class DriveCoreController{private final DriveCoreService service;public DriveCoreController(DriveCoreService service){this.service=service;}
 @PostMapping("/api/core/drive/documents")ApiResponse<DriveCoreService.Document>create(@Valid@RequestBody DriveCoreService.CreateDocumentRequest r){return ApiResponse.ok(service.create(r));}
 @GetMapping("/api/core/drive/documents")ApiResponse<List<DriveCoreService.Document>>list(@RequestParam(required=false)String classification){return ApiResponse.ok(service.documents(classification));}
 @GetMapping("/api/core/drive/documents/{id}")ApiResponse<DriveCoreService.Document>detail(@PathVariable Long id){return ApiResponse.ok(service.detail(id));}
 @GetMapping("/api/core/drive/documents/{id}/versions")ApiResponse<List<DriveCoreService.DocumentVersion>>versions(@PathVariable Long id){return ApiResponse.ok(service.versions(id));}
 @PostMapping("/api/core/drive/documents/{id}/versions")ApiResponse<DriveCoreService.DocumentVersion>version(@PathVariable Long id,@Valid@RequestBody DriveCoreService.VersionRequest r){return ApiResponse.ok(service.addVersion(id,r));}
 @PostMapping("/api/admin/core/drive/documents/{id}/archive")ApiResponse<DriveCoreService.Document>archive(@PathVariable Long id){return ApiResponse.ok(service.archive(id));}
 @PostMapping("/api/admin/core/drive/documents/{id}/shares")ApiResponse<DriveCoreService.SecureShare>share(@PathVariable Long id,@Valid@RequestBody DriveCoreService.CreateShareRequest r){return ApiResponse.ok(service.createShare(id,r));}
 @PostMapping("/api/public/drive/shares/{token}/access")ApiResponse<DriveCoreService.ShareAccess>access(@PathVariable String token,@Valid@RequestBody DriveCoreService.ShareAccessRequest r){return ApiResponse.ok(service.access(token,r));}
 @PostMapping("/api/admin/core/drive/shares/{id}/revoke")ApiResponse<DriveCoreService.SecureShare>revoke(@PathVariable Long id){return ApiResponse.ok(service.revoke(id));}
}
