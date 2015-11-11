# CDRetainer
(A ProxyPool Retainer on Python)   
- You set several urls.
- then **CDRetainer** will crawl free proxylist from network *automatically*.
- After filter function, make a proxylist for each target.  
> [20151111] Make it As an independent Project Stop Updating. 
> Jump to [CDRetainer](https://github.com/okcd00/CDRetainer)
-------------------

### Usage    

```python
git clone https://github.com/okcd00/CDRetainer.git
cd CDRetainer
vim sourcelist.txt # Change conf by yourself, default 3 targets
vim conf/Basic.conf # Change paras by yourself, default run once per 43200 seconds
nohup python CDRetainer.py &
# Then get Result in `./data`
```

### Dependency
+ [Python](http://www.python.org/)
+ urllib2
+ ConfigParser
