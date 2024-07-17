package KMA.TTCS.UserService.Security;

public class ThongBao {
    private String noiDung;
    private String khacPhuc;

    public ThongBao(String noiDung, String khacPhuc) {
        this.noiDung = noiDung;
        this.khacPhuc = khacPhuc;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getKhacPhuc() {
        return khacPhuc;
    }

    public void setKhacPhuc(String khacPhuc) {
        this.khacPhuc = khacPhuc;
    }
}
