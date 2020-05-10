/**
 *
 */
package com.bogokj.live.model;

import com.bogokj.hybrid.model.BaseActModel;

import java.util.List;

/**
 * @author Administrator
 * @date 2016-5-17 下午6:54:44
 */
public class App_GuardianActModel extends BaseActModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private List<LiveGuardianModel> guardian;

    private List<LiveGuardianRulesModel> guardian_rules;

    private int guard_reserve;  //守护底价

    public List<LiveGuardianModel> getGuardian() {
        return guardian;
    }

    public void setGuardian(List<LiveGuardianModel> guardian) {
        this.guardian = guardian;
    }

    public List<LiveGuardianRulesModel> getGuardian_rules() {
        return guardian_rules;
    }

    public void setGuardian_rules(List<LiveGuardianRulesModel> guardian_rules) {
        this.guardian_rules = guardian_rules;
    }


    public int getGuard_reserve() {
        return guard_reserve;
    }

    public void setGuard_reserve(int guard_reserve) {
        this.guard_reserve = guard_reserve;
    }

}
