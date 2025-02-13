-- email과 nickname의 UNIQUE 인덱스 삭제
ALTER TABLE member
    DROP INDEX UKmbmcqelty0fbrvxp1q58dn57t,
    DROP INDEX UKhh9kg6jti4n1eoiertn2k6qsc;