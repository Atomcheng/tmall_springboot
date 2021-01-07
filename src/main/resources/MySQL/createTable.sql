#创建用户表
CREATE TABLE user(
                     id int NOT NULL AUTO_INCREMENT,
                     name varchar(255) DEFAULT NULL,
                     password varchar(255) DEFAULT NULL,
                     salt varchar(255) DEFAULT NULL,
                     PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#创建分类表
CREATE TABLE category(
                         id int NOT NULL AUTO_INCREMENT,
                         name VARCHAR(255) DEFAULT NULL,
                         PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#创建属性表
CREATE TABLE property(
                         id int NULL AUTO_INCREMENT,
                         cid int DEFAULT NULL,
                         name VARCHAR(255) DEFAULT NULL,
                         PRIMARY KEY (id),
                         CONSTRAINT fk_property_category FOREIGN KEY(cid)
                             REFERENCES category(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#创建产品表
CREATE TABLE product(
                        id int NOT NULL AUTO_INCREMENT,
                        name VARCHAR(255) DEFAULT NULL,
                        subTitle VARCHAR (255) DEFAULT NULL,
                        originalPrice float DEFAULT NULL,
                        promotePrice float DEFAULT NULL,
                        stock int(11) DEFAULT NULL,
                        cid int(11) DEFAULT NULL,
                        createDate datetime DEFAULT NULL,
                        PRIMARY KEY (id),
                        CONSTRAINT fk_product_category FOREIGN KEY (cid)
                            REFERENCES category(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#创建属性值表
CREATE TABLE propertyvalue(
                              id int(11) NOT NULL AUTO_INCREMENT,
                              pid int(11) DEFAULT NULL,
                              ptid int(11) DEFAULT NULL,
                              value varchar(255) DEFAULT NULL,
                              PRIMARY KEY(id),
                              CONSTRAINT fk_propertyvalue_product FOREIGN KEY(pid)
                                  REFERENCES product (id),
                              CONSTRAINT fk_propertyvalue_property FOREIGN KEY(ptid)
                                  REFERENCES property (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#创建产品图片表
CREATE TABLE productimage(
                             id int(11) NOT NULL AUTO_INCREMENT,
                             pid int(11) DEFAULT NULL,
                             type VARCHAR(255) DEFAULT NULL,
                             PRIMARY KEY (id),
                             CONSTRAINT fk_productimage_product FOREIGN KEY (pid)
                                 REFERENCES product (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#创建评价表
CREATE TABLE review(
                       id int(11) NOT NULL AUTO_INCREMENT,
                       content TEXT(4000) DEFAULT NULL,
                       uid int(11) DEFAULT NULL,
                       pid int(11) DEFAULT NULL,
                       createDate datetime DEFAULT NULL,
                       PRIMARY KEY (id),
                       CONSTRAINT fk_review_user FOREIGN KEY (uid)
                           REFERENCES user (id),
                       CONSTRAINT fk_review_product FOREIGN KEY (pid)
                           REFERENCES product (id)

) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#创建订单表
CREATE TABLE order_(
                       id int(11) NOT NULL AUTO_INCREMENT,
                       orderCode varchar(255) DEFAULT NULL,
                       address varchar(255) DEFAULT NULL,
                       post varchar(255) DEFAULT NULL,
                       receive varchar(255) DEFAULT NULL,
                       mobile varchar(255) DEFAULT NULL,
                       userMessage varchar(255) DEFAULT NULL,
                       createDate DATETIME DEFAULT NULL,
                       payDate DATETIME DEFAULT NULL,
                       deliveryDate DATETIME DEFAULT NULL,
                       confirmDate DATETIME DEFAULT NULL,
                       uid int(11) DEFAULT NULL,
                       status DATETIME DEFAULT NULL,
                       PRIMARY KEY (id),
                       CONSTRAINT fk_order_user FOREIGN KEY (uid)
                           REFERENCES user (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#创建订单项表
CREATE TABLE orderitem(
                          id int(11) NOT NULL AUTO_INCREMENT,
                          pid int(11) DEFAULT NULL,
                          oid int(11) DEFAULT NULL,
                          uid int(11) DEFAULT NULL,
                          number int(11) DEFAULT NULL,
                          PRIMARY KEY (id),
                          CONSTRAINT fk_orderitem_product FOREIGN KEY (pid) REFERENCES product (id),
                          CONSTRAINT fk_orderitem_user FOREIGN KEY (uid) REFERENCES user (id),
                          CONSTRAINT fk_orderitem_order FOREIGN KEY (oid) REFERENCES order_ (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;