SELECT T.LOAITHUOC, TT.SOLUONG, TT.CACHDUNG
FROM SYS.QLPK_TOATHUOC TT, SYS.QLPK_THUOC T
WHERE TT.MATHUOC=T.MATHUOC AND TT.MAHD=$P{maHD}