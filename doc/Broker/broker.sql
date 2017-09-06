create table customer
       (ssn varchar2(50) primary key,
       cust_name varchar2(50) not null,
       address varchar2(100) not null ) ; 
 
create table stock
       (symbol varchar2(20) primary key,
       price number(10,2) not null) ; 
 
create table shares
       (ssn varchar2(30) not null,
       symbol varchar2(20) not null,
       quantity number(10) not null) ; 
 
insert into stock ( symbol,price) values( 'SUNW', 68.75);
insert into stock ( symbol,price) values( 'CyAs', 22.675);
insert into stock ( symbol,price) values( 'DUKE', 6.25);
insert into stock ( symbol,price) values( 'ABStk', 18.5);
insert into stock ( symbol,price) values( 'JSVco', 9.125);
insert into stock ( symbol,price) values( 'TMAs', 82.375);
insert into stock ( symbol,price) values( 'BWInc', 11.375);
insert into stock ( symbol,price) values( 'GMEnt', 44.625);
insert into stock ( symbol,price) values( 'PMLtd', 203.375);
insert into stock ( symbol,price) values( 'JDK', 33.5);
insert into customer values( '111-111', 'Yufirst1', 'Seoul');
insert into customer values( '111-112', 'Yufirst2', 'Seoul');
insert into customer values( '111-113', 'Yufirst3', 'Seoul');
insert into customer values( '111-114', 'Yufirst4', 'Seoul');
insert into customer values( '111-115', 'yufirst5', 'JeonJu');
insert into customer values( '111-116', 'Yufirst6', 'Seoul');
insert into customer values( '111-117', 'Yufirst7', 'Seoul');
insert into customer values( '111-118', 'Yufirst8', 'Seoul');
insert into customer values( '111-119', 'Yufirst9', 'Seoul');
commit;.


/////////////////////Shares 테이블에 foreign key 추가하는 방법//////////////////////////////////////

1.
ALTER TABLE shares ADD CONSTRAINT fk_shares_ssn foreign key(ssn) references customer(ssn);
ALTER TABLE shares ADD CONSTRAINT fk_shares_symbol foreign key(symbol) references stock(symbol);

2.
자식을 둔 부몬느 삭제할 수 없음을 알았다.
부모가 죽을 수 있는 방법은 자식을 먼저 죽이고 죽는 방법이 있다.
ON DELETE CASCASDE
ON DELETE SET NULL

3. 위 부분을 하기 위해서는 제약조건을 먼저 DROP 시킨 후 가능하다.
::
ALTER TABLE shares DROP constraint fk_shares_ssn;

-->
ALTER TABLE shares ADD CONSTRAINT fk_shares_ssn foreign key(ssn) references customer(ssn) ON DELETE CASCADE;

