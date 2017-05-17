import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public final class DectaTests extends Tests {
    protected String getCaseNumber() {
        return "DectaTests";
    }

    @Test
    public void smsNot3dMerchantSide() throws Exception {
        caseName = "decta_no3d_merch_side";
        card = config.cards.decta3d;

        super.smsNot3dMerchantSide();
    }

    @Test
    public void sms3dMerchantSide() throws Exception {
        caseName = "decta_3d_merch_side";
        card = config.cards.decta3d;
        params3d = config.params.params3d.decta;

        super.sms3dMerchantSide();
    }

    @Test
    public void dmsNot3dMerchantSide() throws Exception {
        caseName = "decta_no3d_merch_side";
        card = config.cards.decta3d;

        super.dmsNot3dMerchantSide();
    }

    @Test
    public void dms3dMerchantSide() throws Exception {
        caseName = "decta_3d_merch_side";
        card = config.cards.decta3d;
        params3d = config.params.params3d.decta;

        super.dms3dMerchantSide();
    }
}
