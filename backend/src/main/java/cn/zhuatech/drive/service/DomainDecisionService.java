/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.drive.service;
import jakarta.validation.constraints.*;
import org.springframework.stereotype.Service;
import java.util.*;
@Service public class DomainDecisionService {
 public DecisionResult assess(DecisionRequest request) { int score=100;List<String> actions=new ArrayList<>();if(!request.checksumVerified()){score-=50;actions.add("重新上传并校验SHA-256");}if(!request.permissionReviewed()){score-=45;actions.add("完成访问权限复核");}if(("CONFIDENTIAL".equals(request.classification())||"SECRET".equals(request.classification()))&&!request.watermarkEnabled()){score-=40;actions.add("对敏感文档启用水印和下载控制");}if(request.shareDays()>30){score-=20;actions.add("缩短外部共享有效期");}if(request.retentionDays()<30){score-=15;actions.add("复核过短的文档保留期限");}return result(score,actions,"SAFE_TO_PUBLISH","SECURITY_REVIEW","BLOCKED",Map.of("fileSizeBytes",request.fileSizeBytes(),"versionCount",request.versionCount(),"classification",request.classification(),"shareDays",request.shareDays(),"retentionDays",request.retentionDays())); }
 private DecisionResult result(int raw,List<String> actions,String good,String warn,String bad,Map<String,Object> metrics) { int score=Math.max(0,Math.min(100,raw));String decision=score>=80?good:score>=50?warn:bad;return new DecisionResult(decision,score,metrics,List.copyOf(actions)); }
 private DecisionResult riskResult(int raw,List<String> actions,String good,String warn,String bad,Map<String,Object> metrics) { int score=Math.max(0,Math.min(100,raw));String decision=score>=70?bad:score>=40?warn:good;return new DecisionResult(decision,score,metrics,List.copyOf(actions)); }
 public record DecisionRequest(
        @NotBlank String fileNo,
        @Positive long fileSizeBytes,
        @Positive int versionCount,
        @Pattern(regexp="PUBLIC|INTERNAL|CONFIDENTIAL|SECRET") String classification,
        @PositiveOrZero int shareDays,
        @Positive int retentionDays,
        boolean checksumVerified,
        boolean permissionReviewed,
        boolean watermarkEnabled) {}
 public record DecisionResult(String decision,int score,Map<String,Object> metrics,List<String> actions) {}
}
