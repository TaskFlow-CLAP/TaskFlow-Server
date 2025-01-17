//package clap.server.adapter.out.persistense.entity.member.constant;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Embeddable;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Embeddable
//public class Password {
//
//    public static final PasswordEncoder ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//
//    @Column(name = "password", nullable = false)
//    private String value;
//
//    private Password(String value) {
//        this.value = value;
//    }
//
//    public static Password encrypt(String value, PasswordEncoder encoder) {
//        return new Password(encoder.encode(value));
//    }
//
//    public boolean isSamePassword(String password, PasswordEncoder encoder) {
//        return encoder.matches(password, this.value);
//    }
//}