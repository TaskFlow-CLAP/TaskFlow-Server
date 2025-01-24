package clap.server.application.port.inbound.management;

public interface AddMainCategoryUsecase {
    void addMainCategory(Long adminId, String code, String name);
}