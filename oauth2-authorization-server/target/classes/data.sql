		-- noinspection SqlNoDataSourceInspectionForFile

INSERT INTO authority  VALUES(1,'ROLE_OAUTH_ADMIN');
INSERT INTO authority VALUES(2,'ROLE_RESOURCE_ADMIN');
INSERT INTO authority VALUES(3,'ROLE_PRODUCT_ADMIN');
INSERT INTO credentials VALUES(1,b'1','oauth_admin','$2a$10$TXYbtrgn5/6hO9xpd/bkPuUJ9abOxXzWrbRzlse0djMUO51vJ2h1i','0');
INSERT INTO credentials VALUES(2,b'1','resource_admin','$2a$10$TXYbtrgn5/6hO9xpd/bkPuUJ9abOxXzWrbRzlse0djMUO51vJ2h1i','0');
INSERT INTO credentials  VALUES(3,b'1','product_admin','$2a$10$TXYbtrgn5/6hO9xpd/bkPuUJ9abOxXzWrbRzlse0djMUO51vJ2h1i','0');
INSERT INTO credentials_authorities VALUE (1,1);
INSERT INTO credentials_authorities VALUE (2,2);
INSERT INTO credentials_authorities VALUE (3,3);

INSERT INTO oauth_client_details VALUES('curl_client','product_api', '$2a$10$TXYbtrgn5/6hO9xpd/bkPuUJ9abOxXzWrbRzlse0djMUO51vJ2h1i', 'read,write', 'client_credentials,authorization_code,refresh_token,password', 'http://127.0.0.1:9091/callback', 'ROLE_PRODUCT_ADMIN', 7200, 1200, NULL, 'true');