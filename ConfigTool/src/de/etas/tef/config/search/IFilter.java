package de.etas.tef.config.search;

import java.nio.file.Path;

public interface IFilter
{
	boolean check(Path file, SearchInfo si);
}
