FROM candlepin-base
MAINTAINER Devan Goodwin <dgoodwin@redhat.com>

# Configure oracle:
# TODO: move this to script later:

# This is a bit slow and expensive so we'll do it as a separate
# layer so the setup script can be tweaked quicker:
RUN wget --no-verbose -r --reject 'index.html*' --no-parent -l 1 http://file.rdu.redhat.com/~dgoodwin/oracle/ -nH -nd -P /root/oracle/

ADD setup-oracle.sh /root/
ADD xe.rsp /root/
ADD launch-oracle.sh /usr/bin/

RUN /bin/bash /root/setup-oracle.sh

CMD ["/usr/bin/cp-test", "-r -o"]
