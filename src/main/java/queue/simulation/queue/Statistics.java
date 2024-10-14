package queue.simulation.queue;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Statistics {
    private int i;
    private BigDecimal prob;
    private BigDecimal population;
    private BigDecimal flow;
    private BigDecimal usage;
    private BigDecimal responseTime;

    public Statistics() {
        this.i = -1;
        this.prob = new BigDecimal(0);
        this.population = new BigDecimal(0);
        this.responseTime = new BigDecimal(0);
        this.flow = new BigDecimal(0);
        this.usage = new BigDecimal(0);
    }
    public Statistics(int i, BigDecimal mi, double prob, int c) {
        this.i = i;
        this.prob = BigDecimal.valueOf(prob);
        this.population = calculatePopulation(i,this.prob);
        this.flow = calculateFlow(mi, i, c, this.prob);
        this.usage = calculateUsage(i, c, this.prob);
        this.responseTime = calculateReponseTime(population, flow);
    }

    public Statistics(BigDecimal prob, BigDecimal population, BigDecimal flow, BigDecimal usage, BigDecimal responseTime) {
        this.i = -1;
        this.prob = prob;
        this.population = population;
        this.flow = flow;
        this.usage = usage;
        this.responseTime = responseTime;
    }

    public Statistics merge(Statistics toMerge) {
        return new Statistics(
                prob.add(toMerge.prob),
                population.add(toMerge.population),
                flow.add(toMerge.flow),
                usage.add(toMerge.usage),
                responseTime.add(toMerge.responseTime)
        );
    }

    public BigDecimal getProb() {
        return prob;
    }

    public int getI() {
        return i;
    }

    public BigDecimal getPopulation() {
        return population;
    }

    public BigDecimal getFlow() {
        return flow;
    }

    public BigDecimal getUsage() {
        return usage;
    }

    public BigDecimal getResponseTime() {
        return responseTime;
    }

    private BigDecimal calculatePopulation(int i, BigDecimal prob) {
        return prob.multiply(BigDecimal.valueOf(i));
    }

    private BigDecimal calculateFlow(BigDecimal mi, int i, int c, BigDecimal prob) {
        BigDecimal serviceRate = BigDecimal.valueOf(Math.min(i, c)).multiply(mi);
        return prob.multiply(serviceRate);
    }

    private BigDecimal calculateUsage(int i, int c, BigDecimal prob) {
        BigDecimal divisionResult = BigDecimal.valueOf(Math.min(i, c))
                .divide(BigDecimal.valueOf(c), 10, RoundingMode.HALF_EVEN);
        return prob.multiply(divisionResult);
    }

    private BigDecimal calculateReponseTime(BigDecimal population, BigDecimal flow) {
        if (flow.doubleValue() == 0) return new BigDecimal(0);
        return population.divide(flow, 10, RoundingMode.HALF_EVEN);
    }
}
