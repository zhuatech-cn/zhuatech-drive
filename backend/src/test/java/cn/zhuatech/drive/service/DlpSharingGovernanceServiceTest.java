/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.drive.service;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class DlpSharingGovernanceServiceTest {
    private final DlpSharingGovernanceService service = new DlpSharingGovernanceService();
    @Test void allowsControlledConfidentialShare() {
        var r = service.evaluate(new DlpSharingGovernanceService.Request("FILE-001", "CONFIDENTIAL", true, true, true, 7, false, true));
        assertEquals("ALLOW", r.decision()); assertTrue(r.linkCreationAllowed());
    }
    @Test void deniesRestrictedFileOnLegalHold() {
        var r = service.evaluate(new DlpSharingGovernanceService.Request("FILE-002", "RESTRICTED", true, false, false, 60, true, true));
        assertEquals("DENY", r.decision()); assertEquals(5, r.blockers().size()); assertFalse(r.linkCreationAllowed());
    }
}
