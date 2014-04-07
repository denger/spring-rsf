package org.springcn.rsf.loadbalancer;


/**
 * LB Rule Factory. Declare & Create LoadBalancer Rule.
 * 
 * @author denger 
 * 7 Apr, 2014
 * 
 */
public class LoadBalancerFactory {
	
	private static LoadBalancerRule DEFAULT_RULE = LoadBalancerRule.Random;

	/**
	 * Create Rule by Name.
	 * 
	 * @param ruleName LoadBalancerRule enum
	 * @return
	 * @see LoadBalancerRule
	 */
	public static AbstractLoadBalancerRule createRule(String ruleName) {
		LoadBalancerRule balancerRule = LoadBalancerRule.getRule(ruleName);
		if(balancerRule == null){
			balancerRule = DEFAULT_RULE;
		}
		return balancerRule.getInstace();
	}

	/**
	 * LoadBalancer Rules Definded.
	 */
	public static enum LoadBalancerRule {
		Random(RandomRule.class), 
		RoundRobin(RandomRule.class);

		public static LoadBalancerRule getRule(String name) {
			if (name == null || name.length() <= 0) {
				return null;
			}
			LoadBalancerRule[] rules = LoadBalancerRule.values();
			for (LoadBalancerRule rule : rules) {
				if (String.valueOf(rule).equalsIgnoreCase(name)) {
					return rule;
				}
			}
			return null;
		}

		Class<? extends AbstractLoadBalancerRule> ruleClassz;

		private LoadBalancerRule(Class<? extends AbstractLoadBalancerRule> clasz) {
			this.ruleClassz = clasz;
		}

		public AbstractLoadBalancerRule getInstace() {
			try {
				return this.ruleClassz.newInstance();
			} catch (Exception e) {
				return null;
			}
		}
		
	}

}
