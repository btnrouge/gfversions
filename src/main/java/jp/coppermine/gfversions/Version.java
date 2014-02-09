package jp.coppermine.gfversions;

/**
 * Representation of an entry of version information.
 */
public class Version {
	
	/**
	 * Component name.
	 */
	private String component;
	
	/**
	 * Component version.
	 */
	private String version;
	
	public Version() { }
	
	public Version(String component, String version) {
		this.component = component;
		this.version = version;
	}

	/* (non Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((component == null) ? 0 : component.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		Version other = (Version) obj;
		if (component == null) {
			if (other.component != null) {
				return false;
			}
		} else if (!component.equals(other.component)) {
			return false;
		}
		if (version == null) {
			if (other.version != null) {
				return false;
			}
		} else if (!version.equals(other.version)) {
			return false;
		}
		return true;
	}

	/* (non Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Version [component=" + component + ", version=" + version + "]";
	}

	/**
	 * Obtains the value of {@code component}.
	 * @return value of {@code component}
	 */
	public String getComponent() {
		return component;
	}

	/**
	 * Set the value of {@code component}.
	 * @param component value of {@code component}
	 */
	public void setComponent(String component) {
		this.component = component;
	}

	/**
	 * Obtains the value of {@code version}.
	 * @return value of {@code version}
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Set the value of {@code version}.
	 * @param version value of {@code version}
	 */
	public void setVersion(String version) {
		this.version = version;
	}
}
