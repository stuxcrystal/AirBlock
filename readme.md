AirBlock
========

AirBlock is a framework that allows its users to easily write Multi-Platform Plugins
without having to implement basic things like handling commands and permissions.

It comes with useful defaults and allows to be extended by a component framework.

Building
--------
Use `mvn clean install` to build the framework.

Usage
-----
The framework is designed to be shaded and relocated inside your plugin to make sure
other plugins that are also using this framework can use different versions of the
framework.

License
-------
This project is licensed under the GNU General Public License v3.
