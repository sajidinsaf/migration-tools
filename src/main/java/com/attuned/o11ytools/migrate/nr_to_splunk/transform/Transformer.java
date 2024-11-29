package com.attuned.o11ytools.migrate.nr_to_splunk.transform;

public interface Transformer<T, V> {

	public V transform(T t);
}
