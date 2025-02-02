package clap.server.application.port.inbound.admin;

public interface AddMainCategoryUsecase {
    void addMainCategory(Long adminId, String code, String name);
}