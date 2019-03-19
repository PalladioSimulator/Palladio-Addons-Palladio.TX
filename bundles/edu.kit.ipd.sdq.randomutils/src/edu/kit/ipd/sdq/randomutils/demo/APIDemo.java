package edu.kit.ipd.sdq.randomutils.demo;

import java.util.ArrayList;
import java.util.List;

import edu.kit.ipd.sdq.randomutils.RandomVariable;
import edu.kit.ipd.sdq.randomutils.factories.EmpiricalDistribution;
import edu.kit.ipd.sdq.randomutils.factories.TheoreticalDistribution;

public class APIDemo {

    public static void main(String[] args) {
        System.out.println("-------------- Poisson:");

        RandomVariable<Integer> pois = TheoreticalDistribution.poisson(25);
        for (int i = 0; i < 10; i++) {
            System.out.println(pois.next());
        }
        
        RandomVariable<Double> r = TheoreticalDistribution.uniform(1.5, 1.7);
        for (int i = 0; i < 10; i++) {
            System.out.println(r.next());
        }

        System.out.println("-------------- SPT 2.0, 1.0, 3.0");

        RandomVariable<Double> s = EmpiricalDistribution.fromSPTHistogram(2.0, 1.0, 3.0);
        for (int i = 0; i < 10; i++) {
            System.out.println(s.next());
        }

        System.out.println("-------------- StoEx DoublePDF[(1.0;0.3)(1.5;0.2)(2.0;0.5)]");

        RandomVariable<Double> t = EmpiricalDistribution.fromStoEx("DoublePDF[(1.0;0.3)(1.5;0.2)(2.0;0.5)]");
        for (int i = 0; i < 10; i++) {
            System.out.println(t.next());
        }

        System.out.println("-------------- String:");

        RandomVariable<String> u = EmpiricalDistribution.string(3, 4);
        for (int i = 0; i < 10; i++) {
            System.out.println(u.next());
        }

        System.out.println("-------------- Uniform List:");

        List<String> list = new ArrayList<String>();
        list.add("First");
        list.add("Second");
        list.add("Third");
        RandomVariable<String> v = EmpiricalDistribution.fromList(list).uniform();
        for (int i = 0; i < 10; i++) {
            System.out.println(v.next());
        }

        System.out.println("-------------- Zipf:");

        RandomVariable<Integer> w = TheoreticalDistribution.zipf(10, 1);
        for (int i = 0; i < 10; i++) {
            System.out.println(w.next());
        }

        System.out.println("-------------- Zipfian List:");

        List<String> list2 = new ArrayList<String>();
        list2.add("First");
        list2.add("Second");
        list2.add("Third");
        RandomVariable<String> x = EmpiricalDistribution.fromList(list2).zipf(1);
        for (int i = 0; i < 10; i++) {
            System.out.println(x.next());
        }

    }

}
