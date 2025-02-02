package clap.server.application.port.inbound.admin;

public interface DeleteCategoryUsecase {
    void deleteCategory(Long adminId, Long categoryId);
}
