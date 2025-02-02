package clap.server.application.port.inbound.admin;

public interface AddCategoryUsecase {
    void addMainCategory(Long adminId, String code, String name);
    void addSubCategory(Long adminId, Long mainCategoryId, String code, String name);
}