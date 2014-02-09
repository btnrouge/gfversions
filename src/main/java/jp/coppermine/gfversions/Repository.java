package jp.coppermine.gfversions;

/**
 * Representation of a repository location.
 */
public class Repository {
	
	/**
	 * Tag name of the repository.
	 */
	private String tag;
	
	/**
	 * Repository location (URL).
	 */
	private String url;
	
	public Repository() { }
	
	public Repository(String tag, String url) {
		this.tag = tag;
		this.url = url;
	}
	
	/**
	 * Returns message formats {@code tag} and {@code url}.
	 * This is used rendering the choice box.
	 * 
	 * @return message formats {@code tag} and {@code url}.
	 */
	@Override
	public String toString() {
		return String.format("%s [%s]", tag, url);
	}

	/* (non Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	/* (non Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Repository other = (Repository) obj;
		if (tag == null) {
			if (other.tag != null) {
				return false;
			}
		} else if (!tag.equals(other.tag)) {
			return false;
		}
		if (url == null) {
			if (other.url != null) {
				return false;
			}
		} else if (!url.equals(other.url)) {
			return false;
		}
		return true;
	}

	/**
	 * Obtains value of {@code tag}.
	 * 
	 * @return value of {@code tag}
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Set value of {@code tag}.
	 * 
	 * @param tag value of {@code tag}
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * Obtains value of {@code url}.
	 * 
	 * @return value of {@code url}
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Set value of {@code url}.
	 * 
	 * @param tag value of {@code url}
	 */
	public void setUrl(String url) {
		this.url = url;
	}

}
