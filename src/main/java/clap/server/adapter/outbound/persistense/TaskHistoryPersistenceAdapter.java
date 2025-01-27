package clap.server.adapter.outbound.persistense;


import clap.server.application.port.outbound.task.LoadTaskHistoryPort;
import clap.server.common.annotation.architecture.PersistenceAdapter;

import lombok.RequiredArgsConstructor;






@PersistenceAdapter
@RequiredArgsConstructor
public class TaskHistoryPersistenceAdapter implements LoadTaskHistoryPort {

}
