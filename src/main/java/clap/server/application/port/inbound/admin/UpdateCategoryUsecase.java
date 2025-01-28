package clap.server.application.port.inbound.admin;

public interface UpdateCategoryUsecase {
    void updateCategory(Long adminId, Long categoryId, String name, String code);
}