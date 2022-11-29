package com.mikuac.shiro.entity;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EpidemicJson implements Serializable {

    @com.alibaba.fastjson2.annotation.JSONField(name = "ret")
    private Integer ret;
    @com.alibaba.fastjson2.annotation.JSONField(name = "info")
    private String info;
    @com.alibaba.fastjson2.annotation.JSONField(name = "data")
    private Data data;

    @lombok.Data
    public static class Data implements Serializable {
        @com.alibaba.fastjson2.annotation.JSONField(name = "diseaseh5Shelf")
        private Diseaseh5Shelf diseaseh5Shelf;

        @lombok.Data
        public static class Diseaseh5Shelf implements Serializable {
            @com.alibaba.fastjson2.annotation.JSONField(name = "chinaAdd")
            private ChinaAdd chinaAdd;
            @com.alibaba.fastjson2.annotation.JSONField(name = "isShowAdd")
            private Boolean isShowAdd;
            @com.alibaba.fastjson2.annotation.JSONField(name = "showAddSwitch")
            private ShowAddSwitch showAddSwitch;
            @com.alibaba.fastjson2.annotation.JSONField(name = "lastUpdateTime")
            private String lastUpdateTime;
            @com.alibaba.fastjson2.annotation.JSONField(name = "chinaTotal")
            private ChinaTotal chinaTotal;
            @com.alibaba.fastjson2.annotation.JSONField(name = "areaTree")
            private List<AreaTree> areaTree;

            @lombok.Data
            public static class ChinaAdd implements Serializable {
                @com.alibaba.fastjson2.annotation.JSONField(name = "noInfectH5")
                private Integer noInfectH5;
                @com.alibaba.fastjson2.annotation.JSONField(name = "confirm")
                private Integer confirm;
                @com.alibaba.fastjson2.annotation.JSONField(name = "dead")
                private Integer dead;
                @com.alibaba.fastjson2.annotation.JSONField(name = "suspect")
                private Integer suspect;
                @com.alibaba.fastjson2.annotation.JSONField(name = "noInfect")
                private Integer noInfect;
                @com.alibaba.fastjson2.annotation.JSONField(name = "localConfirm")
                private Integer localConfirm;
                @com.alibaba.fastjson2.annotation.JSONField(name = "localConfirmH5")
                private Integer localConfirmH5;
                @com.alibaba.fastjson2.annotation.JSONField(name = "heal")
                private Integer heal;
                @com.alibaba.fastjson2.annotation.JSONField(name = "nowConfirm")
                private Integer nowConfirm;
                @com.alibaba.fastjson2.annotation.JSONField(name = "nowSevere")
                private Integer nowSevere;
                @com.alibaba.fastjson2.annotation.JSONField(name = "importedCase")
                private Integer importedCase;
            }

            @lombok.Data
            public static class ShowAddSwitch implements Serializable {
                @com.alibaba.fastjson2.annotation.JSONField(name = "all")
                private Boolean all;
                @com.alibaba.fastjson2.annotation.JSONField(name = "dead")
                private Boolean dead;
                @com.alibaba.fastjson2.annotation.JSONField(name = "heal")
                private Boolean heal;
                @com.alibaba.fastjson2.annotation.JSONField(name = "nowSevere")
                private Boolean nowSevere;
                @com.alibaba.fastjson2.annotation.JSONField(name = "noInfect")
                private Boolean noInfect;
                @com.alibaba.fastjson2.annotation.JSONField(name = "localinfeciton")
                private Boolean localinfeciton;
                @com.alibaba.fastjson2.annotation.JSONField(name = "confirm")
                private Boolean confirm;
                @com.alibaba.fastjson2.annotation.JSONField(name = "suspect")
                private Boolean suspect;
                @com.alibaba.fastjson2.annotation.JSONField(name = "nowConfirm")
                private Boolean nowConfirm;
                @com.alibaba.fastjson2.annotation.JSONField(name = "importedCase")
                private Boolean importedCase;
                @com.alibaba.fastjson2.annotation.JSONField(name = "localConfirm")
                private Boolean localConfirm;
            }

            @lombok.Data
            public static class ChinaTotal implements Serializable {
                @com.alibaba.fastjson2.annotation.JSONField(name = "confirm")
                private Integer confirm;
                @com.alibaba.fastjson2.annotation.JSONField(name = "nowSevere")
                private Integer nowSevere;
                @com.alibaba.fastjson2.annotation.JSONField(name = "local_acc_confirm")
                private Integer localAccConfirm;
                @com.alibaba.fastjson2.annotation.JSONField(name = "localConfirmAdd")
                private Integer localConfirmAdd;
                @com.alibaba.fastjson2.annotation.JSONField(name = "mRiskTime")
                private String mRiskTime;
                @com.alibaba.fastjson2.annotation.JSONField(name = "heal")
                private Integer heal;
                @com.alibaba.fastjson2.annotation.JSONField(name = "showLocalConfirm")
                private Integer showLocalConfirm;
                @com.alibaba.fastjson2.annotation.JSONField(name = "showlocalinfeciton")
                private Integer showlocalinfeciton;
                @com.alibaba.fastjson2.annotation.JSONField(name = "noInfectH5")
                private Integer noInfectH5;
                @com.alibaba.fastjson2.annotation.JSONField(name = "mediumRiskAreaNum")
                private Integer mediumRiskAreaNum;
                @com.alibaba.fastjson2.annotation.JSONField(name = "dead")
                private Integer dead;
                @com.alibaba.fastjson2.annotation.JSONField(name = "noInfect")
                private Integer noInfect;
                @com.alibaba.fastjson2.annotation.JSONField(name = "confirmAdd")
                private Integer confirmAdd;
                @com.alibaba.fastjson2.annotation.JSONField(name = "deadAdd")
                private Integer deadAdd;
                @com.alibaba.fastjson2.annotation.JSONField(name = "mtime")
                private String mtime;
                @com.alibaba.fastjson2.annotation.JSONField(name = "highRiskAreaNum")
                private Integer highRiskAreaNum;
                @com.alibaba.fastjson2.annotation.JSONField(name = "nowConfirm")
                private Integer nowConfirm;
                @com.alibaba.fastjson2.annotation.JSONField(name = "localConfirm")
                private Integer localConfirm;
                @com.alibaba.fastjson2.annotation.JSONField(name = "suspect")
                private Integer suspect;
                @com.alibaba.fastjson2.annotation.JSONField(name = "importedCase")
                private Integer importedCase;
                @com.alibaba.fastjson2.annotation.JSONField(name = "localConfirmH5")
                private Integer localConfirmH5;
                @com.alibaba.fastjson2.annotation.JSONField(name = "localWzzAdd")
                private Integer localWzzAdd;
                @com.alibaba.fastjson2.annotation.JSONField(name = "nowLocalWzz")
                private Integer nowLocalWzz;
            }

            @lombok.Data
            public static class AreaTree implements Serializable {
                @com.alibaba.fastjson2.annotation.JSONField(name = "name")
                private String name;
                @com.alibaba.fastjson2.annotation.JSONField(name = "today")
                private Today today;
                @com.alibaba.fastjson2.annotation.JSONField(name = "total")
                private Total total;
                @com.alibaba.fastjson2.annotation.JSONField(name = "children")
                private List<ChildrenX> children;

                @lombok.Data
                public static class Today implements Serializable {
                    @com.alibaba.fastjson2.annotation.JSONField(name = "confirm")
                    private Integer confirm;
                    @com.alibaba.fastjson2.annotation.JSONField(name = "isUpdated")
                    private Boolean isUpdated;
                }

                @lombok.Data
                public static class Total implements Serializable {
                    @com.alibaba.fastjson2.annotation.JSONField(name = "nowConfirm")
                    private Integer nowConfirm;
                    @com.alibaba.fastjson2.annotation.JSONField(name = "wzz")
                    private Integer wzz;
                    @com.alibaba.fastjson2.annotation.JSONField(name = "provinceLocalConfirm")
                    private Integer provinceLocalConfirm;
                    @com.alibaba.fastjson2.annotation.JSONField(name = "showHeal")
                    private Boolean showHeal;
                    @com.alibaba.fastjson2.annotation.JSONField(name = "mtime")
                    private String mtime;
                    @com.alibaba.fastjson2.annotation.JSONField(name = "adcode")
                    private String adcode;
                    @com.alibaba.fastjson2.annotation.JSONField(name = "confirm")
                    private Integer confirm;
                    @com.alibaba.fastjson2.annotation.JSONField(name = "heal")
                    private Integer heal;
                    @com.alibaba.fastjson2.annotation.JSONField(name = "continueDayZeroLocalConfirmAdd")
                    private Integer continueDayZeroLocalConfirmAdd;
                    @com.alibaba.fastjson2.annotation.JSONField(name = "highRiskAreaNum")
                    private Integer highRiskAreaNum;
                    @com.alibaba.fastjson2.annotation.JSONField(name = "continueDayZeroLocalConfirm")
                    private Integer continueDayZeroLocalConfirm;
                    @com.alibaba.fastjson2.annotation.JSONField(name = "dead")
                    private Integer dead;
                    @com.alibaba.fastjson2.annotation.JSONField(name = "showRate")
                    private Boolean showRate;
                    @com.alibaba.fastjson2.annotation.JSONField(name = "mediumRiskAreaNum")
                    private Integer mediumRiskAreaNum;
                }

                @lombok.Data
                public static class ChildrenX implements Serializable {
                    @com.alibaba.fastjson2.annotation.JSONField(name = "today")
                    private TodayX today;
                    @com.alibaba.fastjson2.annotation.JSONField(name = "total")
                    private TotalX total;
                    @com.alibaba.fastjson2.annotation.JSONField(name = "name")
                    private String name;
                    @com.alibaba.fastjson2.annotation.JSONField(name = "adcode")
                    private String adcode;
                    @com.alibaba.fastjson2.annotation.JSONField(name = "date")
                    private String date;
                    @com.alibaba.fastjson2.annotation.JSONField(name = "children")
                    private List<Children> children;

                    @lombok.Data
                    public static class TodayX implements Serializable {
                        @com.alibaba.fastjson2.annotation.JSONField(name = "wzz_add")
                        private Integer wzzAdd;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "local_confirm_add")
                        private Integer localConfirmAdd;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "abroad_confirm_add")
                        private Integer abroadConfirmAdd;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "dead_add")
                        private Integer deadAdd;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "confirm")
                        private Integer confirm;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "confirmCuts")
                        private Integer confirmCuts;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "isUpdated")
                        private Boolean isUpdated;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "tip")
                        private String tip;
                    }

                    @lombok.Data
                    public static class TotalX implements Serializable {
                        @com.alibaba.fastjson2.annotation.JSONField(name = "showRate")
                        private Boolean showRate;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "heal")
                        private Integer heal;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "highRiskAreaNum")
                        private Integer highRiskAreaNum;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "confirm")
                        private Integer confirm;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "dead")
                        private Integer dead;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "mediumRiskAreaNum")
                        private Integer mediumRiskAreaNum;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "continueDayZeroLocalConfirmAdd")
                        private Integer continueDayZeroLocalConfirmAdd;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "mtime")
                        private String mtime;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "adcode")
                        private String adcode;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "nowConfirm")
                        private Integer nowConfirm;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "provinceLocalConfirm")
                        private Integer provinceLocalConfirm;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "continueDayZeroConfirmAdd")
                        private Integer continueDayZeroConfirmAdd;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "continueDayZeroConfirm")
                        private Integer continueDayZeroConfirm;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "showHeal")
                        private Boolean showHeal;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "wzz")
                        private Integer wzz;
                    }

                    @lombok.Data
                    public static class Children implements Serializable {
                        @com.alibaba.fastjson2.annotation.JSONField(name = "total")
                        private TotalXX total;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "name")
                        private String name;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "adcode")
                        private String adcode;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "date")
                        private String date;
                        @com.alibaba.fastjson2.annotation.JSONField(name = "today")
                        private TodayXX today;

                        @lombok.Data
                        public static class TotalXX implements Serializable {
                            @com.alibaba.fastjson2.annotation.JSONField(name = "confirm")
                            private Integer confirm;
                            @com.alibaba.fastjson2.annotation.JSONField(name = "mediumRiskAreaNum")
                            private Integer mediumRiskAreaNum;
                            @com.alibaba.fastjson2.annotation.JSONField(name = "continueDayZeroLocalConfirm")
                            private Integer continueDayZeroLocalConfirm;
                            @com.alibaba.fastjson2.annotation.JSONField(name = "wzz")
                            private Integer wzz;
                            @com.alibaba.fastjson2.annotation.JSONField(name = "highRiskAreaNum")
                            private Integer highRiskAreaNum;
                            @com.alibaba.fastjson2.annotation.JSONField(name = "mtime")
                            private String mtime;
                            @com.alibaba.fastjson2.annotation.JSONField(name = "nowConfirm")
                            private Integer nowConfirm;
                            @com.alibaba.fastjson2.annotation.JSONField(name = "dead")
                            private Integer dead;
                            @com.alibaba.fastjson2.annotation.JSONField(name = "heal")
                            private Integer heal;
                            @com.alibaba.fastjson2.annotation.JSONField(name = "showHeal")
                            private Boolean showHeal;
                            @com.alibaba.fastjson2.annotation.JSONField(name = "provinceLocalConfirm")
                            private Integer provinceLocalConfirm;
                            @com.alibaba.fastjson2.annotation.JSONField(name = "adcode")
                            private String adcode;
                            @com.alibaba.fastjson2.annotation.JSONField(name = "showRate")
                            private Boolean showRate;
                            @com.alibaba.fastjson2.annotation.JSONField(name = "continueDayZeroLocalConfirmAdd")
                            private Integer continueDayZeroLocalConfirmAdd;
                        }

                        @lombok.Data
                        public static class TodayXX implements Serializable {
                            @com.alibaba.fastjson2.annotation.JSONField(name = "isUpdated")
                            private Boolean isUpdated;
                            @com.alibaba.fastjson2.annotation.JSONField(name = "wzz_add")
                            private String wzzAdd;
                            @com.alibaba.fastjson2.annotation.JSONField(name = "local_confirm_add")
                            private Integer localConfirmAdd;
                            @com.alibaba.fastjson2.annotation.JSONField(name = "confirm")
                            private Integer confirm;
                            @com.alibaba.fastjson2.annotation.JSONField(name = "confirmCuts")
                            private Integer confirmCuts;
                        }
                    }
                }
            }
        }
    }
}