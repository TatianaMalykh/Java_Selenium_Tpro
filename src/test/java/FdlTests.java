import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@Ignore
@RunWith(BlockJUnit4ClassRunner.class)
public final class FdlTests extends Tests {
    protected String getCaseNumber() {
        return "FdlTests";
    }

    @Test
    public void smsNot3dMerchantSide() throws Exception {
        caseName = "fdl_no3d_merch_side";
        card = config.cards.fdl3d;

        super.smsNot3dMerchantSide();
    }

    @Test
    public void sms3dMerchantSide() throws Exception {
        caseName = "fdl_3d_merch_side";
        card = config.cards.fdl3d;
        params3d = config.params.params3d.fdl;

        super.sms3dMerchantSide();
    }

    @Test
    public void dmsNot3dMerchantSide() throws Exception {
        caseName = "fdl_no3d_merch_side";
        card = config.cards.fdl3d;

        super.dmsNot3dMerchantSide();
    }

    @Test
    public void dms3dMerchantSide() throws Exception {
        caseName = "fdl_3d_merch_side";
        card = config.cards.fdl3d;
        params3d = config.params.params3d.fdl;

        super.dms3dMerchantSide();
    }
}
