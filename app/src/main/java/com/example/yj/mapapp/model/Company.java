package com.example.yj.mapapp.model;

import java.util.List;

public class Company {

    /**
     * m :
     * s : 1
     * d : [{"id":1183,"name":"上海颖异机械厂","pointLong":"121.426141","pointLat":"30.967139"},{"id":1181,"name":"上海鸿丰阀门有限公司","pointLong":"121.484017","pointLat":"30.923193"},{"id":1169,"name":"上海凯克涂料有限公司","pointLong":"121.317209","pointLat":"31.107967"},{"id":1168,"name":"上海典来涂料有限公司","pointLong":"121.366104","pointLat":"31.358914"},{"id":1167,"name":"上海达尔诺涂料科技有限公司","pointLong":"121.23184","pointLat":"30.94356"},{"id":1166,"name":"上海三协实业有限公司","pointLong":"121.433897","pointLat":"31.338506"},{"id":1165,"name":"上海炫杰环保涂料有限公司","pointLong":"121.354987","pointLat":"31.312341"},{"id":1164,"name":"星庄园化工（上海）有限公司","pointLong":"121.35612","pointLat":"31.317539"},{"id":1158,"name":"上海詹森实业有限公司","pointLong":"121.453605","pointLat":"31.099471"},{"id":1157,"name":"上海豹翔特种门窗有限公司","pointLong":"121.489665","pointLat":"31.088454"},{"id":1155,"name":"上海昂高门窗有限公司","pointLong":"121.367248","pointLat":"30.994295"},{"id":1154,"name":"上海嘉苑铝塑门窗有限公司","pointLong":"121.220867","pointLat":"31.358591"},{"id":1153,"name":"上海锦澄实业有限公司","pointLong":"121.202349","pointLat":"31.316629"},{"id":1152,"name":"上海杰思工程实业有限公司","pointLong":"121.206971","pointLat":"31.320917"},{"id":1151,"name":"上海龙毓建材有限公司","pointLong":"121.185095","pointLat":"31.007311"},{"id":1150,"name":"上海韵霓新型材料有限公司","pointLong":"121.452553","pointLat":"30.81651"},{"id":1149,"name":"上海科胜幕墙有限公司","pointLong":"121.327219","pointLat":"31.144421"},{"id":1148,"name":"上海嘉宝莉涂料有限公司","pointLong":"121.29088","pointLat":"30.728878"},{"id":1147,"name":"上海科罗纳精细化工有限公司","pointLong":"121.294866","pointLat":"30.736902"},{"id":1146,"name":"上海建瓶化工科技有限公司","pointLong":"121.321059","pointLat":"31.35543"},{"id":1145,"name":"上海爱森德涂料有限公司","pointLong":"121.343516","pointLat":"30.840419"},{"id":1144,"name":"亚创庆军实业（上海）有限公司","pointLong":"121.303542","pointLat":"31.223585"},{"id":1142,"name":"上海旌翔建材科技有限公司","pointLong":"121.359448","pointLat":"30.776765"},{"id":1141,"name":"上海华侠化工科技有限公司","pointLong":"121.417303","pointLat":"30.80004"},{"id":1140,"name":"上海柯伟化工科技有限公司","pointLong":"121.294202","pointLat":"31.060286"},{"id":1139,"name":"上海中南建筑材料有限公司","pointLong":"121.292533","pointLat":"31.152162"},{"id":1138,"name":"上海希诺建筑材料有限公司","pointLong":"121.319963","pointLat":"30.956459"},{"id":1137,"name":"上海威德沃涂料有限公司","pointLong":"121.436218","pointLat":"31.140217"},{"id":1136,"name":"上海克络蒂材料科技发展有限公司","pointLong":"121.265944","pointLat":"31.086066"},{"id":1135,"name":"上海欧瑞涂料有限公司","pointLong":"121.362734","pointLat":"31.366376"},{"id":1133,"name":"上海麦斯特建工高科技建筑化工有限公司","pointLong":"121.398066","pointLat":"30.999705"},{"id":1132,"name":"玖润塑胶工业（上海）有限公司","pointLong":"121.290389","pointLat":"31.232826"},{"id":1131,"name":"上海阜邦建筑门窗有限公司","pointLong":"121.194726","pointLat":"30.986749"},{"id":1130,"name":"上海研和门窗系统有限公司","pointLong":"121.349956","pointLat":"30.832272"},{"id":1129,"name":"上海同百幕墙门窗工程有限公司","pointLong":"121.353243","pointLat":"30.779732"},{"id":1128,"name":"上海鼎坚门窗幕墙有限公司","pointLong":"121.106814","pointLat":"30.876504"},{"id":1127,"name":"上海海螺水泥有限责任公司","pointLong":"121.452295","pointLat":"31.009585"},{"id":1126,"name":"上海迪探节能科技有限公司","pointLong":"121.279813","pointLat":"31.094369"}]
     */

    private String m;
    private int s;
    /**
     * id : 1183
     * name : 上海颖异机械厂
     * pointLong : 121.426141
     * pointLat : 30.967139
     */

    private List<DEntity> d;

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public List<DEntity> getD() {
        return d;
    }

    public void setD(List<DEntity> d) {
        this.d = d;
    }

    public static class DEntity {
        private int id;
        private String name;
        private String pointLong;
        private String pointLat;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPointLong() {
            return pointLong;
        }

        public void setPointLong(String pointLong) {
            this.pointLong = pointLong;
        }

        public String getPointLat() {
            return pointLat;
        }

        public void setPointLat(String pointLat) {
            this.pointLat = pointLat;
        }
    }
}
