package de.etas.tef.config.search;

import java.nio.file.Path;

public interface ISearchEngine {
	void search(Path directory, String searchWord);
}
