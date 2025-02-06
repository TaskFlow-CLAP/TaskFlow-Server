package clap.server.adapter.inbound.web.dto.task.request;

public enum SortBy {
        DEFAULT("기본순"),
        CONTRIBUTE("기여도순");

        private final String value;

        SortBy(String value) {
                this.value = value;
        }

        public String getValue() {
                return value;
        }
}

