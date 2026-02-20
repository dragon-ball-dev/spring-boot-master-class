package com.springmasterclass.study.no_spring;

public class ToPho {
    private BanhPho banhPho;
    private ThitBo thitBo;

    public ToPho() {
        // Vấn đề: Tightly Couple là bị trói chặt vào nhau. Cực kì khó bảo trì code
        // Giải quyết vấn đề này Spring sẽ sử dụng IOC
        // Giao toàn quyền cho một ông chủ nhà hàng hoặc chủ nhà => Spring Container ( Quản lý Bean )
        // Thì các bạn muốn nó giao nguyên liệu để làm tô phở thì nó sẽ dùng DI ( Thêm phụ thuộc )
        this.banhPho = new BanhPho();
        this.thitBo = new ThitBo();
    }

    public void phucVu() {
        System.out.println("🍲 Phuc vu to pho gom: " + banhPho.layBanhPho() + " va " + thitBo.layThitBo());
    }
}