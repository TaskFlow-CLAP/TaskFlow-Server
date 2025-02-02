package clap.server.application.port.inbound.admin;

public interface AddSubCategoryUsecase {
    void addSubCategory(Long adminId, Long mainCategoryId, String code, String name);
}
