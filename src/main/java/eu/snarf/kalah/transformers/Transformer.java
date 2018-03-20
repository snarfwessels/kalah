package eu.snarf.kalah.transformers;

/**
 * @author Frans on 3/19/2018.
 */
public interface Transformer<From, To> {

    To apply(From from);
}